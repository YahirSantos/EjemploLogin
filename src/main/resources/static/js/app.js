// Elementos principales
const showLoginButton = document.getElementById("showLogin");
const showRegisterButton = document.getElementById("showRegister");

const loginSection = document.getElementById("loginSection");
const registerSection = document.getElementById("registerSection");

const loginForm = document.getElementById("loginForm");
const registerForm = document.getElementById("registerForm");

const loginMessage = document.getElementById("loginMessage");
const registerMessage = document.getElementById("registerMessage");


// Alternar entre formularios
showLoginButton.addEventListener("click", () => {
    loginSection.hidden = false;
    registerSection.hidden = true;

    loginMessage.textContent = "";
    registerMessage.textContent = "";
});

showRegisterButton.addEventListener("click", () => {
    loginSection.hidden = true;
    registerSection.hidden = false;

    loginMessage.textContent = "";
    registerMessage.textContent = "";
});


// Mostrar u ocultar contraseñas
const passwordButtons = document.querySelectorAll(".togglePassword");

passwordButtons.forEach((button) => {
    button.addEventListener("click", () => {
        const inputId = button.dataset.target;
        const passwordInput = document.getElementById(inputId);

        const isHidden = passwordInput.type === "password";

        passwordInput.type = isHidden ? "text" : "password";

        button.textContent = isHidden
            ? "Ocultar contraseña"
            : "Mostrar contraseña";

        button.setAttribute("aria-pressed", String(isHidden));
    });
});

// Registrarse
registerForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    registerMessage.textContent = "Registrando usuario...";

    const formData = new FormData(registerForm);

    const requestData = {
        username: formData.get("username"),
        email: formData.get("email"),
        password: formData.get("password")
    };

    try {
        const response = await fetch("/api/auth/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)
        });

        const data = await response.json();

        if (!response.ok) {
            registerMessage.textContent =
                data.error || data.message || "No se pudo registrar el usuario.";

            return;
        }

        registerMessage.textContent =
            data.message || "Usuario registrado correctamente.";

        registerForm.reset();

    } catch (error) {
        registerMessage.textContent =
            "No se pudo conectar con el servidor.";
    }
});


// Iniciar sesión
loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    loginMessage.textContent = "Verificando credenciales...";

    const formData = new FormData(loginForm);

    const requestData = {
        username: formData.get("username"),
        password: formData.get("password")
    };

    try {
        const response = await fetch("/api/auth/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(requestData)
        });

        const data = await response.json();

        if (!response.ok) {
            loginMessage.textContent =
                data.error || "No se pudo iniciar sesión.";

            return;
        }

        loginMessage.textContent = data.message;

    } catch (error) {
        loginMessage.textContent =
            "No se pudo conectar con el servidor.";
    }
});