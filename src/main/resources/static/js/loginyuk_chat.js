const chatMessages = document.getElementById('chatMessages');
const messageInput = document.getElementById('messageInput');
const sendBtn = document.getElementById('sendBtn');

const roomId = "1";

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

function connect() {

    if (stompClient && stompClient.connected) {
        console.log("Sudah terkoneksi, skip connect()");
        return;
    }

    const socket = new SockJS("/ws-chat");
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // Unsubscribe dulu kalau sudah ada (cegah double)
        if (subscription) {
            subscription.unsubscribe();
            subscription = null;
        }

        // Simpan reference subscription ← ini yang kurang sebelumnya
        subscription = stompClient.subscribe(`/topic/rooms/${roomId}`, function(message) {
            console.log("RAW", message.body);
            const msg = JSON.parse(message.body);
            const isMine = msg.sender === getCurrentUsername();
            renderBubble(msg.content, isMine);
        });

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

    const username = getCurrentUsername();
    const userId = getUserId();

    const chatMessage = {
        sender: username,
        senderId: userId,
        content: isi,
        roomId: roomId,
        type: 'CHAT'
    };

    stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
    messageInput.value = '';
}

function loadUserProfile() {
    if (!token) return;

    fetch('/api/user/me', {
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
loadUserProfile()