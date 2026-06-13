const chatMessages = document.getElementById('chatMessages');
const messageInput = document.getElementById('messageInput');
const sendBtn = document.getElementById('sendBtn');

const roomId = "1";

// JWT must be obtained from your login flow (e.g. localStorage)
const token = localStorage.getItem('jwt');

let stompClient = null;

function renderBubble(isi, isMine) {
    const row = document.createElement('div');
    row.classList.add('bubble-row', isMine ? 'mine' : 'theirs');

    const bubble = document.createElement('div');
    bubble.classList.add('bubble', isMine ? 'mine' : 'theirs');
    bubble.textContent = isi;

    row.appendChild(bubble);
    chatMessages.appendChild(row);
    scrollToBottom();
}

function scrollToBottom() {
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

function connect() {
    const socket = new SockJS("/ws-chat");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/topic/rooms/${roomId}`, function(message) {

            console.log("RAW", message.body);

            const msg = JSON.parse(message.body);

            console.log(msg);

            const isMine = msg.sender === getCurrentUsername();

            renderBubble(msg.content, isMine);
        });
        /*
        stompClient.subscribe(`/topic/rooms/${roomId}`, function (message) {
            const msg = JSON.parse(message.body);
            const isMine = msg.sender === getCurrentUsername();
            renderBubble(msg.content, isMine);
        });
         
         */
    }, function (error) {
        console.error('STOMP error:', error);
    });
}

function getCurrentUsername() {
    // Decode from JWT or store separately at login
    if (!token) return null;
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
}

function sendMessage() {
    const isi = messageInput.value.trim();
    if (isi === '' || !stompClient) return;

    const chatMessage = {
        content: isi,
        roomId: roomId,
        type: 'CHAT'
    };

    stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
    messageInput.value = '';
}

sendBtn.addEventListener('click', sendMessage);

messageInput.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') sendMessage();
});

connect();