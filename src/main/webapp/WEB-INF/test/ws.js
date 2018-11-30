var stompClient = null;

function setConnected(connected) {
	document.getElementById('connect').disabled = connected;
	document.getElementById('disconnect').disabled = !connected;
	document.getElementById('calculationDiv').style.visibility = connected ? 'visible'
			: 'hidden';
	document.getElementById('calResponse').innerHTML = '';
}

function connect() {
	var socket = new SockJS('http://118.178.224.151:5001/socket');
	stompClient = Stomp.over(socket);
	
	var token = '6c30539a89f442269cdc885bd3b76974';

	// stompClient.connect(params1,params2,params3)
	// params1:表示客户端的认证信息
	// params2:表示连接成功时（服务器响应 CONNECTED 帧）的回调方法
	// params3:表示连接失败时（服务器响应 ERROR 帧）的回调方法，非必须；
	stompClient.connect({
		"Token" : token
	}, function(frame) {
		// 连接成功时（服务器响应 CONNECTED 帧）的回调方法
		setConnected(true);
		console.log('Connected: ' + frame);
		// 订阅一个消息
		stompClient.subscribe('/user/queue/status', function(calResult) {
			// 服务器响应
			console.log(calResult);
			// showResult(JSON.parse(calResult.body).result);
			showResult(JSON.parse(calResult.body));
		});
	});
}

function disconnect() {
	stompClient.disconnect(function() {
		alert("断开连接！");
	});
	setConnected(false);
	console.log("Disconnected");
}

function sendNum() {
	var num1 = document.getElementById('num1').value;
	var num2 = document.getElementById('num2').value;

	// stompClient.send(destination url, headers, body); 客户端发送消息给服务器
	// destination url 为服务器 controller中 @MessageMapping 中匹配的URL，字符串，必须参数
	// headers 为发送信息的header，JavaScript 对象，可选参数
	// body 为发送信息的 body，字符串，可选参数
	stompClient.send("/user/queue/status", {}, JSON.stringify({
		'num1' : num1,
		'num2' : num2
	}));
}

function showResult(message) {
	var response = document.getElementById('calResponse');
	p.style.wordWrap = 'break-word';

	message.forEach(function(i, index) {
		var p = document.createElement('p');
		p.appendChild(document.createTextNode(i['location.lat'] + '|'));
		p.appendChild(document.createTextNode(i['location.lng'] + '|'));
		p.appendChild(document.createTextNode(i['carId'] + '|'));
		p.appendChild(document.createTextNode(i['status'] + '|'));
	});

	response.appendChild(p);
}