const chatMessages = document.getElementById('chatMessages');
const messageInput = document.getElementById('messageInput');
const sendBtn = document.getElementById('sendBtn');

// Ganti port sesuai server.port backend Spring Boot kamu (cek application.properties)
const API_BASE = "http://localhost:8080";

const roomId = "1"; // hardcode dulu
const token = localStorage.getItem('jwt');

let stompClient = null;
let subscription = null;

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

function loadMessages(roomId) {
    fetch(`${API_BASE}/api/chat/history/${roomId}`, {
        headers: { 'Authorization': 'Bearer ' + token }
    })
    .then(res => res.json())
    .then(messages => {
        chatMessages.innerHTML = ''; // clear dulu
        messages.forEach(msg => {
            const isMine = String(msg.senderId) === String(getUserId());
            renderBubble(msg.isi, isMine);
        });
    })
    .catch(err => console.error('Gagal load messages:', err));
}

function connect() {
    if (stompClient && stompClient.connected) {
        console.log("Sudah terkoneksi, skip connect()");
        return;
    }

    const socket = new SockJS(`${API_BASE}/ws-chat`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        if (subscription) {
            subscription.unsubscribe();
            subscription = null;
        }

        subscription = stompClient.subscribe(`/topic/rooms/${roomId}`, function(message) {
            console.log("RAW", message.body);
            const msg = JSON.parse(message.body);
            const isMine = String(msg.senderId) === String(getUserId());
            renderBubble(msg.content, isMine);
        });

        loadMessages(roomId); // ← pakai roomId bukan currentRoomId
    
    }, function (error) {
        console.error('STOMP error:', error);
    });
}

function getCurrentUsername() {
    if (!token) return 'anonymous';
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.sub;
    } catch (e) {
        return 'anonymous';
    }
}

function getUserId() {
    if (!token) return null;
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.userId;
    } catch (e) {
        return null;
    }
}

function sendMessage() {
    const isi = messageInput.value.trim();
    if (isi === '' || !stompClient) return;

    const chatMessage = {
        sender: getCurrentUsername(),
        senderId: getUserId(),
        content: isi,
        roomId: roomId,
        type: 'CHAT'
    };

    stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
    messageInput.value = '';
}

function loadUserProfile() {
    if (!token) return;

    fetch(`${API_BASE}/api/user/me`, {
        headers: { 'Authorization': 'Bearer ' + token }
    })
    .then(res => res.json())
    .then(user => {
        const photo = user.profilePhoto ?? 'assets/default-avatar.png';
        document.getElementById('profilePhoto').src = photo;
    })
    .catch(err => console.error('Gagal load profile:', err));
}

sendBtn.addEventListener('click', sendMessage);

messageInput.addEventListener('keypress', function (e) {
    if (e.key === 'Enter') sendMessage();
});

connect();
loadUserProfile();