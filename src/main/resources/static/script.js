// Complete Frontend JavaScript
// ==========================================

// Common API URL
const API_URL = "http://localhost:8080/api";

// Room images
const roomImages = {
    Standard: "https://images.unsplash.com/photo-1611892440504-42a792e24d32",
    Deluxe: "https://images.unsplash.com/photo-1590490360182-c33d57733427",
    Suite: "https://images.unsplash.com/photo-1582719508461-905c673771fd"
};

function bookRoom(roomType) {
    localStorage.setItem("selectedRoom", roomType);
    localStorage.removeItem("selectedRoomId");
    window.location.href = "booking.html";
}



function selectRoom(roomId, roomType) {
    localStorage.setItem("selectedRoomId", roomId);
    localStorage.setItem("selectedRoom", roomType);

    window.location.href = "booking.html";
}


async function loadRooms() {

    const container = document.getElementById("roomsContainer");

    if (!container) return;

    const count = document.getElementById("roomCount");
    const message = document.getElementById("roomMessage");

    try {

        const response = await fetch(
            `${API_URL}/rooms/available`
        );

        if (!response.ok) {
            throw new Error("Unable to fetch rooms.");
        }

        const rooms = await response.json();

        container.innerHTML = "";

        if (count) {
            count.textContent = `${rooms.length} available room(s)`;
        }

        if (rooms.length === 0) {
            container.textContent = "No available rooms currently.";
            return;
        }

        rooms.forEach(room => {

            const image =
                roomImages[room.roomType] || roomImages.Standard;

            const card = document.createElement("article");
            card.className = "dynamic-room-card";

            const img = document.createElement("img");
            img.src = image;
            img.alt = `${room.roomType} hotel room`;

            const info = document.createElement("div");
            info.className = "dynamic-room-info";

            const status = document.createElement("span");
            status.className = "room-status";
            status.textContent = "Available";

            const title = document.createElement("h3");
            title.textContent = `${room.roomType} Room`;

            const description = document.createElement("p");
            description.textContent =
                `Enjoy a comfortable stay in our ${room.roomType} room.`;

            const price = document.createElement("h4");
            price.textContent =
                `₹${Number(room.price).toLocaleString("en-IN")} / night`;

            const button = document.createElement("button");
            button.textContent = "Book This Room";

            button.addEventListener("click", () => {
                selectRoom(room.id, room.roomType);
            });

            info.append(status, title, description, price, button);
            card.append(img, info);

            container.appendChild(card);
        });

    } catch (error) {

        container.innerHTML = "";

        if (count) {
            count.textContent = "Rooms unavailable";
        }

        if (message) {
            message.textContent =
                "Unable to load rooms. Please check the server.";
        }

        console.error("Room loading error:", error);
    }
}


// ==========================================
// 3. BOOKING PAGE
// ==========================================

let bookingRooms = [];


async function loadBookingRooms() {

    const roomSelect = document.getElementById("roomId");

    if (!roomSelect) return;

    try {

        const response = await fetch(
            `${API_URL}/rooms/available`
        );

        if (!response.ok) {
            throw new Error("Could not load rooms.");
        }

        bookingRooms = await response.json();

        roomSelect.innerHTML =
            '<option value="">Select a room</option>';

        bookingRooms.forEach(room => {

            const option = document.createElement("option");

            option.value = room.id;

            option.textContent =
                `${room.roomType} - ₹${room.price} per night`;

            roomSelect.appendChild(option);
        });

        // Select the room chosen on the Rooms page
        const selectedRoomId =
            localStorage.getItem("selectedRoomId");

        const selectedRoomType =
            localStorage.getItem("selectedRoom");

        if (selectedRoomId) {

            roomSelect.value = selectedRoomId;

        } else if (selectedRoomType) {

            const matchingRoom = bookingRooms.find(
                room => room.roomType === selectedRoomType
            );

            if (matchingRoom) {
                roomSelect.value = matchingRoom.id;
            }
        }

        updateBookingTotal();

    } catch (error) {

        roomSelect.innerHTML =
            '<option value="">Unable to load rooms</option>';

        showBookingMessage(
            "Unable to load rooms. Please try again.",
            "red"
        );

        console.error("Booking room error:", error);
    }
}


// Calculate booking amount
function updateBookingTotal() {

    const roomId = Number(
        document.getElementById("roomId")?.value
    );

    const checkInValue =
        document.getElementById("checkIn")?.value;

    const checkOutValue =
        document.getElementById("checkOut")?.value;

    const totalElement =
        document.getElementById("totalAmount");

    if (!totalElement) return;

    const room = bookingRooms.find(
        r => r.id === roomId
    );

    if (!room || !checkInValue || !checkOutValue) {

        totalElement.textContent = "₹0";
        return;
    }

    const checkIn = new Date(checkInValue + "T00:00:00");
    const checkOut = new Date(checkOutValue + "T00:00:00");

    const nights = Math.round(
        (checkOut - checkIn) / (1000 * 60 * 60 * 24)
    );

    if (nights <= 0) {

        totalElement.textContent = "Invalid dates";
        return;
    }

    const total = nights * Number(room.price);

    totalElement.textContent =
        `₹${total.toLocaleString("en-IN")}`;
}


// Display booking messages
function showBookingMessage(message, color) {

    const element =
        document.getElementById("bookingMessage");

    if (element) {
        element.textContent = message;
        element.style.color = color;
    }
}


// Submit booking
async function submitBooking(event) {

    event.preventDefault();

    const button = document.getElementById("bookButton");
    button.disabled = true;

    try {

        const name =
            document.getElementById("guestName").value.trim();

        const email =
            document.getElementById("guestEmail").value.trim();

        const phone =
            document.getElementById("guestPhone").value.trim();

        const roomId =
            Number(document.getElementById("roomId").value);

        const checkIn =
            document.getElementById("checkIn").value;

        const checkOut =
            document.getElementById("checkOut").value;

        if (!roomId) {
            throw new Error("Please select a room.");
        }

        if (!checkIn || !checkOut) {
            throw new Error("Please select both dates.");
        }

        const checkInDate =
            new Date(checkIn + "T00:00:00");

        const checkOutDate =
            new Date(checkOut + "T00:00:00");

        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (checkInDate <= today) {
            throw new Error("Check-in must be a future date.");
        }

        if (checkOutDate <= checkInDate) {
            throw new Error("Check-out must be after check-in.");
        }

        showBookingMessage(
            "Registering guest...",
            "#173c35"
        );

        // Register customer

// Find existing customer first
        let customer;

        const lookupResponse = await fetch(
            `${API_URL}/customers/email/${encodeURIComponent(email)}`
        );

        if (lookupResponse.ok) {

            // Customer already exists
            customer = await lookupResponse.json();

        } else if (lookupResponse.status === 404) {

            // Register new customer
            const customerResponse = await fetch(
                `${API_URL}/customers`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        name: name,
                        email: email,
                        phone: phone
                    })
                }
            );

            if (!customerResponse.ok) {
                throw new Error("Customer registration failed.");
            }

            customer = await customerResponse.json();

        } else {
            throw new Error("Unable to verify customer details.");
        }


        // Create booking
        showBookingMessage(
            "Creating your booking...",
            "#173c35"
        );

        const params = new URLSearchParams({
            customerId: customer.id,
            roomId: roomId,
            checkIn: checkIn,
            checkOut: checkOut
        });

        const bookingResponse = await fetch(
            `${API_URL}/bookings?${params.toString()}`,
            {
                method: "POST"
            }
        );

        if (!bookingResponse.ok) {

            const errorText = await bookingResponse.text();

            throw new Error(
                errorText || "Booking failed."
            );
        }

        const booking = await bookingResponse.json();

        // Save booking information
        localStorage.setItem("customerId", customer.id);
        localStorage.setItem("bookingId", booking.id);
        localStorage.setItem("bookingTotal", booking.totalAmount);

        showBookingMessage(
            "Booking confirmed! Booking ID: " + booking.id,
            "green"
        );

        // Go to payment page
        setTimeout(() => {
            window.location.href = "payment.html";
        }, 1500);

    } catch (error) {

        showBookingMessage(error.message, "red");

        console.error("Booking error:", error);

    } finally {

        button.disabled = false;
    }
}


// ==========================================
// 4. PAYMENT PAGE
// ==========================================

function showPaymentMessage(message, color) {

    const element =
        document.getElementById("paymentMessage");

    if (element) {
        element.textContent = message;
        element.style.color = color;
    }
}


async function submitPayment(event) {

    event.preventDefault();

    const button = document.getElementById("payButton");
    button.disabled = true;

    try {

        const bookingId =
            localStorage.getItem("bookingId");

        if (!bookingId) {
            throw new Error(
                "Booking ID not found. Please make a booking first."
            );
        }

        const selectedMethod = document.querySelector(
            'input[name="paymentMethod"]:checked'
        );

        if (!selectedMethod) {
            throw new Error("Please select a payment method.");
        }

        const paymentMethod = selectedMethod.value;

        showPaymentMessage(
            "Processing payment...",
            "#173c35"
        );

        const response = await fetch(
            `${API_URL}/payments/${bookingId}?paymentMethod=${paymentMethod}&simulateSuccess=true`,
            {
                method: "POST"
            }
        );

        if (!response.ok) {

            const errorText = await response.text();

            throw new Error(
                errorText || "Payment failed."
            );
        }

        const payment = await response.json();

        if (payment.paymentStatus !== "SUCCESS") {
            throw new Error("Payment was not successful.");
        }

        localStorage.setItem("paymentId", payment.id);

        localStorage.setItem(
            "paymentStatus",
            payment.paymentStatus
        );

        showPaymentMessage(
            "Payment successful! Your reservation is confirmed.",
            "green"
        );

        setTimeout(() => {
            window.location.href = "my-bookings.html";
        }, 1800);

    } catch (error) {

        showPaymentMessage(error.message, "red");

        console.error("Payment error:", error);

    } finally {

        button.disabled = false;
    }
}


// ==========================================
// 5. MY BOOKINGS PAGE
// ==========================================

function showMyBookingMessage(message, color) {

    const element =
        document.getElementById("myBookingMessage");

    if (element) {
        element.textContent = message;
        element.style.color = color;
    }
}


async function loadMyBookings() {

    const container =
        document.getElementById("bookingsContainer");

    if (!container) return;

    const customerId =
        localStorage.getItem("customerId");

    if (!customerId) {

        container.textContent =
            "No customer account found. Please complete a booking first.";

        return;
    }

    container.textContent = "Loading your bookings...";

    try {

        const response = await fetch(
            `${API_URL}/bookings/customer/${customerId}`
        );

        if (!response.ok) {
            throw new Error("Unable to fetch bookings.");
        }

        const bookings = await response.json();

        container.innerHTML = "";

        if (bookings.length === 0) {

            container.textContent =
                "You have no bookings yet.";

            return;
        }

        bookings.forEach(booking => {

            const card = document.createElement("div");
            card.className = "booking-history-card";

            const heading = document.createElement("h3");
            heading.textContent = `Booking #${booking.id}`;

            const room = document.createElement("p");

            room.textContent =
                `Room: ${booking.room?.roomType || "Room details unavailable"}`;

            const checkIn = document.createElement("p");
            checkIn.textContent =
                `Check-in: ${booking.checkIn}`;

            const checkOut = document.createElement("p");
            checkOut.textContent =
                `Check-out: ${booking.checkOut}`;

            const amount = document.createElement("p");

            amount.textContent =
                `Total Amount: ₹${Number(booking.totalAmount).toLocaleString("en-IN")}`;

            const status = document.createElement("span");
            status.className = "booking-status";

            if (booking.status === "CANCELLED") {
                status.classList.add("cancelled");
            }

            status.textContent =
                `Status: ${booking.status}`;

            card.append(
                heading,
                room,
                checkIn,
                checkOut,
                amount,
                status
            );

            // Show cancel button only for confirmed bookings
            if (booking.status === "CONFIRMED") {

                const cancelButton =
                    document.createElement("button");

                cancelButton.className = "cancel-booking";

                cancelButton.textContent = "Cancel Booking";

                cancelButton.addEventListener("click", () => {
                    cancelBooking(booking.id);
                });

                card.appendChild(cancelButton);
            }

            container.appendChild(card);
        });

    } catch (error) {

        container.textContent = error.message;

        console.error("My bookings error:", error);
    }
}


// Cancel booking
async function cancelBooking(bookingId) {

    const confirmed = confirm(
        `Are you sure you want to cancel booking #${bookingId}?`
    );

    if (!confirmed) return;

    try {

        const response = await fetch(
            `${API_URL}/bookings/${bookingId}/cancel`,
            {
                method: "PUT"
            }
        );

        if (!response.ok) {
            throw new Error("Unable to cancel booking.");
        }

        showMyBookingMessage(
            "Booking cancelled successfully.",
            "green"
        );

        loadMyBookings();

    } catch (error) {

        showMyBookingMessage(
            error.message,
            "red"
        );
    }
}


// ==========================================
// 6. PAGE INITIALIZATION
// ==========================================

document.addEventListener("DOMContentLoaded", function () {

    // Rooms Page
    if (document.getElementById("roomsContainer")) {
        loadRooms();
    }

    // Booking Page
    if (document.getElementById("bookingForm")) {

        loadBookingRooms();

        document.getElementById("roomId")
            .addEventListener("change", updateBookingTotal);

        document.getElementById("checkIn")
            .addEventListener("change", updateBookingTotal);

        document.getElementById("checkOut")
            .addEventListener("change", updateBookingTotal);

        document.getElementById("bookingForm")
            .addEventListener("submit", submitBooking);
    }

    // Payment Page
    if (document.getElementById("paymentForm")) {

        const bookingId =
            localStorage.getItem("bookingId");

        const amount =
            localStorage.getItem("bookingTotal");

        document.getElementById("bookingId").textContent =
            bookingId || "Not available";

        document.getElementById("paymentAmount").textContent =
            amount
                ? `₹${Number(amount).toLocaleString("en-IN")}`
                : "₹0";

        document.getElementById("paymentForm")
            .addEventListener("submit", submitPayment);
    }

    // My Bookings Page
    if (document.getElementById("refreshBookings")) {

        loadMyBookings();

        document.getElementById("refreshBookings")
            .addEventListener("click", loadMyBookings);
    }

});

console.log("GrandStay JavaScript Loaded Successfully!");
