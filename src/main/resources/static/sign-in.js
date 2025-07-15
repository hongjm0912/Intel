document.getElementById('loginForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    try {
        const res = await fetch('/login/oauth2', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        if (!res.ok) throw new Error('로그인 실패');
        const data = await res.json();
        localStorage.setItem('oauthToken', data.token);
        alert('로그인 성공');
        window.location.href = '/gesipan';
    } catch (err) {
        alert(err.message);
    }
});
