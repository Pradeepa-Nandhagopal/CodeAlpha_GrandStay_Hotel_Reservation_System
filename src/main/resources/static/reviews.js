
const API_URL = "http://localhost:8080/api";

// Load all reviews
async function loadReviews() {
 const list = document.getElementById("reviewList");

 if (!list) return;

 try {
  list.textContent = "Loading reviews...";

  const response = await fetch(`${API_URL}/reviews`);

  if (!response.ok) {
   throw new Error("Could not load reviews.");
  }

  const data = await response.json();

  list.innerHTML = "";

  if (data.length === 0) {
   list.textContent = "No reviews available yet.";
   return;
  }

  data.forEach(review => {
   const item = document.createElement("article");
   item.className = "room";

   const heading = document.createElement("h3");
   heading.textContent =
       `${"★".repeat(review.rating)} (${review.rating}/5)`;

   const comment = document.createElement("p");
   comment.textContent = review.comment || "No comment";

   item.append(heading, comment);
   list.appendChild(item);
  });

 } catch (error) {
  list.textContent = error.message;
  console.error("Review loading error:", error);
 }
}


// Submit a review
document.addEventListener("DOMContentLoaded", () => {

 loadReviews();

 const form = document.getElementById("reviewForm");

 if (!form) return;

 form.addEventListener("submit", async event => {
  event.preventDefault();

  const message = document.getElementById("reviewMessage");

  const review = {
   customerId: Number(
       document.getElementById("customerId").value
   ),
   bookingId: Number(
       document.getElementById("bookingId").value
   ),
   rating: Number(
       document.getElementById("rating").value
   ),
   comment:
       document.getElementById("comment").value.trim()
  };

  try {
   message.textContent = "Submitting review...";

   const response = await fetch(`${API_URL}/reviews`, {
    method: "POST",
    headers: {
     "Content-Type": "application/json"
    },
    body: JSON.stringify(review)
   });

   const responseText = await response.text();

   let data = {};

   try {
    data = responseText ? JSON.parse(responseText) : {};
   } catch {
    throw new Error("Server returned an invalid response.");
   }

   if (!response.ok) {
    throw new Error(
        data.error || responseText || "Could not submit review."
    );
   }

   message.textContent = "Review submitted successfully!";

   form.reset();

   await loadReviews();

  } catch (error) {
   message.textContent = error.message;
   console.error("Review submission error:", error);
  }
 });
});

