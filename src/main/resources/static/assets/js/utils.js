function togglePassword() {
    const input = document.getElementById('password');
    const eye = document.getElementById('toggleEye');
    if (input.type === "password") {
        input.type = "text";
        eye.classList.replace('bi-eye', 'bi-eye-slash');
    } else {
        input.type = "password";
        eye.classList.replace('bi-eye-slash', 'bi-eye');
    }
}