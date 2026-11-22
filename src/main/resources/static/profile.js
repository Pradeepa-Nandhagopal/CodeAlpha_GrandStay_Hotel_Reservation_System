
const API_URL = "http://localhost:8080/api";

// Send request to Spring Boot
async function send(url, body) {
 const response = await fetch(`${API_URL}${url}`, {
  method: "POST",
  headers: {
   "Content-Type": "application/json"
  },
  body: JSON.stringify(body)
 });

 const responseText = await response.text();

 let data = {};

 try {
  data = responseText ? JSON.parse(responseText) : {};
 } catch {
  throw new Error("Invalid server response. Check Spring Boot.");
 }

 if (!response.ok) {
  throw new Error(
      data.message || data.error || "Request failed."
  );
 }

 return data;
}

// Initialize account page
document.addEventListener("DOMContentLoaded", () => {

 // REGISTER
 const registerForm = document.getElementById("registerForm");

 if (registerForm) {
  registerForm.addEventListener("submit", async event => {
   event.preventDefault();

   const button = registerForm.querySelector(
       "button[type='submit']"
   );

   const message = document.getElementById("registerMessage");

   button.disabled = true;

   try {
    message.textContent = "Creating account...";

    const email = document.getElementById(
        "registerEmail"
    ).value.trim();

    const password = document.getElementById(
        "registerPassword"
    ).value;

    const data = await send("/accounts/register", {
     email: email,
     password: password
    });

    message.textContent =
        data.message || "Account created successfully!";

    registerForm.reset();

    showLogin();

    document.getElementById("loginMessage").textContent =
        "Account created successfully. Please sign in.";

   } catch (error) {
    message.textContent = error.message;
    console.error("Registration error:", error);

   } finally {
    button.disabled = false;
   }
  });
 }

 // LOGIN
 const loginForm = document.getElementById("loginForm");

 if (loginForm) {
  loginForm.addEventListener("submit", async event => {
   event.preventDefault();

   const button = loginForm.querySelector(
       "button[type='submit']"
   );

   const message = document.getElementById("loginMessage");

   button.disabled = true;

   try {
    message.textContent = "Signing in...";

    const email = document.getElementById(
        "loginEmail"
    ).value.trim();

    const password = document.getElementById(
        "loginPassword"
    ).value;

    const data = await send("/accounts/login", {
     email: email,
     password: password
    });

    localStorage.setItem("customerId", data.customerId);

    message.textContent = "Login successful!";

    setTimeout(() => {
     window.location.href = "my-bookings.html";
    }, 1000);

   } catch (error) {
    message.textContent = error.message;
    console.error("Login error:", error);

   } finally {
    button.disabled = false;
   }
  });
 }
});
