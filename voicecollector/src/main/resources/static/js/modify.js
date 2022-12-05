// set up basic variables for app

const record = document.querySelector('#recordBtn');
const stop = document.querySelector('#stopBtn');
const play = document.querySelector('#playBtn');
const next = document.querySelector('#nextBtn');

const soundClips = document.querySelector('#soundClips');

const canvas = document.querySelector('#audioVisualizer');

const collectionId = document.querySelector('#collectionId').value;
let blob = null;
let audioURL = null;
let audioType = 'wav';

// disable stop button while not recording

//stop.disabled = true;

// visualiser setup - create web audio api context and canvas

let audioCtx;
const canvasCtx = canvas.getContext("2d");

//main block for doing the audio recording

if (navigator.mediaDevices.getUserMedia) {
	console.log('getUserMedia supported.');

	const constraints = { audio: true };
	let chunks = [];

	let onSuccess = function(stream) {
		const mediaRecorder = new MediaRecorder(stream);

		visualize(stream);

		record.onclick = function() {

			if (audioURL != null) window.URL.revokeObjectURL(audioURL);

			mediaRecorder.start();
			console.log(mediaRecorder.state);
			console.log("recorder started");
			record.style.background = "red";

			stop.disabled = false;
			record.disabled = true;
			play.disabled = true;
			next.disabled = true;

			const clip = document.querySelector('.clip');
			if (clip != null) clip.remove();

			play.style.display = 'none';
			stop.style.display = 'block';
		}

		stop.onclick = function() {
			mediaRecorder.stop();
			console.log(mediaRecorder.state);
			console.log("recorder stopped");
			record.style.background = "";
			record.style.color = "";
			// mediaRecorder.requestData();

			stop.disabled = true;
			record.disabled = false;
			play.disabled = false;
			next.disabled = false;

			play.style.display = 'block';
			stop.style.display = 'none';
		}

		mediaRecorder.onstop = function(e) {
			console.log("data available after MediaRecorder.stop() called.");

			const clipContainer = document.createElement('article');
			const audio = document.createElement('audio');
			audio.classList.add('recorded');

			const clipLabel = document.createElement('p');
			const clipName = collectionId;
			clipLabel.textContent = clipName;

			clipContainer.classList.add('clip');
			audio.setAttribute('controls', '');

			clipContainer.appendChild(audio);
			clipContainer.appendChild(clipLabel);
			soundClips.appendChild(clipContainer);

			audio.controls = true;
			blob = new Blob(chunks, { 'type': 'audio/' + audioType + '; codecs=opus' }); // audio/ogg, audio/wav, audio/mp3
			chunks = [];
			audioURL = window.URL.createObjectURL(blob);
			audio.src = audioURL;
			console.log("recorder stopped");
		}

		play.onclick = function() {
			const audio = document.querySelector('.recorded');
			if (audio != null) {
				audio.play();
				
				play.disabled = true;
				play.style.background = "black";
				
				audio.onended = function() {
					play.disabled = false;
					play.style.background = "";
					play.style.color = "";
				};
			}
		}


		next.onclick = function() {
			const reader = new FileReader();
			reader.readAsDataURL(blob);
			reader.onloadend = () => {
				const base64Data = reader.result;
				console.log(base64Data)

				uploadFile(base64Data)
			}
		}

		async function uploadFile(base64Data) {
			let formData = new FormData();
			formData.append("collectionId", collectionId);
			formData.append("audioType", audioType);
			formData.append("base64Data", base64Data);
			let response = await fetch('/collection/modify', {
				method: "POST",
				body: formData
			});

			if (response.status == 200) {
				alert("정상적으로 재녹음되었습니다.");
				const page = document.getElementById("page").value;
				const kw = document.getElementById("kw").value;
				window.location.href = "/collection/list" + "?page=" + page + "&kw=" + kw;
			} else {
				alert("재녹음 문제가 발생하였습니다.");
			}
		}

		mediaRecorder.ondataavailable = function(e) {
			chunks.push(e.data);
		}
	}

	let onError = function(err) {
		console.log('The following error occured: ' + err);
	}

	navigator.mediaDevices.getUserMedia(constraints).then(onSuccess, onError);

} else {
	console.log('getUserMedia not supported on your browser!');
}

function visualize(stream) {
	if (!audioCtx) {
		audioCtx = new AudioContext();
	}

	const source = audioCtx.createMediaStreamSource(stream);

	const analyser = audioCtx.createAnalyser();
	analyser.fftSize = 2048;
	const bufferLength = analyser.frequencyBinCount;
	const dataArray = new Uint8Array(bufferLength);

	source.connect(analyser);
	//analyser.connect(audioCtx.destination);

	draw()

	function draw() {
		const WIDTH = canvas.width
		const HEIGHT = canvas.height;

		requestAnimationFrame(draw);

		analyser.getByteTimeDomainData(dataArray);

		canvasCtx.fillStyle = 'rgb(255, 255, 255)';
		canvasCtx.fillRect(0, 0, WIDTH, HEIGHT);

		canvasCtx.lineWidth = 2;
		canvasCtx.strokeStyle = 'rgb(0, 0, 0)';

		canvasCtx.beginPath();

		let sliceWidth = WIDTH * 1.0 / bufferLength;
		let x = 0;


		for (let i = 0; i < bufferLength; i++) {

			let v = dataArray[i] / 128.0;
			let y = v * HEIGHT / 2;

			if (i === 0) {
				canvasCtx.moveTo(x, y);
			} else {
				canvasCtx.lineTo(x, y);
			}

			x += sliceWidth;
		}

		canvasCtx.lineTo(canvas.width, canvas.height / 2);
		canvasCtx.stroke();

	}
}

window.onload = function(){
	// collectionId가 없는 경우 버튼 disbled 처리
	if(document.getElementById('collectionId').value == "0") {
		record.disabled = true;
		stop.disabled = true;
		play.disabled = true;
		next.disabled = true;
	}
	
	// audio 생성	
	const clipContainer = document.createElement('article');
	const audio = document.createElement('audio');
	audio.classList.add('recorded');

	const clipLabel = document.createElement('p');
	const clipName = collectionId;
	clipLabel.textContent = clipName;

	clipContainer.classList.add('clip');
	audio.setAttribute('controls', '');

	clipContainer.appendChild(audio);
	clipContainer.appendChild(clipLabel);
	soundClips.appendChild(clipContainer);

	const base64Data = document.getElementById('base64Data').value;
	let binary = convertURIToBinary(base64Data);
	let blob = new Blob([binary], {
	  type: 'audio/wav'
	});
	let blobUrl = URL.createObjectURL(blob);
	audio.src = blobUrl;
	audio.controls = true;
}

// function : convert base64 to blob
function convertURIToBinary(dataURI) {
  let BASE64_MARKER = ';base64,';
  let base64Index = dataURI.indexOf(BASE64_MARKER) + BASE64_MARKER.length;
  let base64 = dataURI.substring(base64Index);
  let raw = window.atob(base64);
  let rawLength = raw.length;
  let arr = new Uint8Array(new ArrayBuffer(rawLength));

  for (let i = 0; i < rawLength; i++) {
    arr[i] = raw.charCodeAt(i);
  }
  return arr;
}