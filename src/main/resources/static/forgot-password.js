
const API_URL = "http://localhost:8080/api";

document.addEventListener("DOMContentLoaded", () => {

    const sendOtpForm = document.getElementById("sendOtpForm");
    const resetForm = document.getElementById("resetPasswordForm");
    const message = document.getElementById("resetMessage");

    let registeredEmail = "";

    // SEND OTP
    sendOtpForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const button = document.getElementById("sendOtpButton");
        registeredEmail = document.getElementById("resetEmail")
            .value.trim();

        button.disabled = true;
        message.textContent = "Sending OTP...";

        try {
            const response = await fetch(
                `${API_URL}/password/send-otp`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        email: registeredEmail
                    })
                }
            );

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || "Unable to send OTP.");
            }

            message.textContent = "OTP sent! Check your email.";
            sendOtpForm.hidden = true;
            resetForm.hidden = false;

        } catch (error) {
            message.textContent = error.message;
        } finally {
            button.disabled = false;
        }
    });

    // RESET PASSWORD
    resetForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const button = document.getElementById("resetButton");

        const otp = document.getElementById("resetOtp").value.trim();
        const newPassword = document.getElementById("newPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;

        if (newPassword !== confirmPassword) {
            message.textContent = "Passwords do not match.";
            return;
        }

        button.disabled = true;
        message.textContent = "Resetting password...";

        try {
            const response = await fetch(
                `${API_URL}/password/reset`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        email: registeredEmail,
                        otp: otp,
                        newPassword: newPassword
                    })
                }
            );

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || "Password reset failed.");
            }

            message.textContent =
                "Password reset successfully! Redirecting to login...";

            resetForm.reset();

            setTimeout(() => {
                window.location.href = "profile.html";
            }, 2000);

        } catch (error) {
            message.textContent = error.message;
        } finally {
            button.disabled = false;
        }
    });

});
