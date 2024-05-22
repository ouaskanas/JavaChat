'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var userList = document.querySelector('#userList');
var toggleDarkModeButton = document.querySelector('#toggle-dark-mode');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


var forbiddenWords = ["anas", "nigger", "emsi"];

function containsForbiddenWords(message) {
    return forbiddenWords.some(word => message.includes(word));
}

function connect(event) {
    username = document.querySelector('#name').value.trim();

    if (username) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {

    stompClient.subscribe('/topic/public', onMessageReceived);

    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({ sender: username, type: 'JOIN' })
    )

    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (containsForbiddenWords(messageContent)) {
        alert("Votre message contient des mots interdits et ne sera pas envoyé.");
    } else if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };

        stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if (message.type === 'JOIN') {
        userList.insertAdjacentHTML('beforeend', '<li>' + message.sender + '</li>');
    } else if (message.type === 'LEAVE') {
        var userItems = userList.getElementsByTagName('li');
        for (var i = 0; i < userItems.length; i++) {
            if (userItems[i].textContent === message.sender) {
                userItems[i].remove();
                break;
            }
        }
    } else {
        var messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('div');
        avatarElement.classList.add('avatar');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        var messageContentElement = document.createElement('div');
        messageContentElement.classList.add('message-content');

        var usernameElement = document.createElement('span');
        usernameElement.classList.add('username');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);

        messageContentElement.appendChild(usernameElement);
        messageContentElement.appendChild(textElement);

        messageElement.appendChild(avatarElement);
        messageElement.appendChild(messageContentElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    document.body.classList.toggle('light-mode');
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
toggleDarkModeButton.addEventListener('click', toggleDarkMode, true);