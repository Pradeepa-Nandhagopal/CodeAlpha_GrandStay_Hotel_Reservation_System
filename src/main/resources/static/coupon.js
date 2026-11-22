document.addEventListener('DOMContentLoaded',()=>{
 const button=document.getElementById('applyCouponButton');if(!button)return;
 button.addEventListener('click',async()=>{
  const code=document.getElementById('couponCode').value.trim();
  const amount=Number(localStorage.getItem('bookingTotal')||0);
  const output=document.getElementById('couponMessage');
  if(!code||amount<=0){output.textContent='Enter a coupon and make sure the booking total is available.';return}
  try{const res=await fetch('/api/coupons/validate',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({code,amount})});
   const data=await res.json();if(!res.ok)throw new Error(data.error||'Coupon validation failed');
   localStorage.setItem('couponCode',data.code);localStorage.setItem('discountedBookingTotal',data.finalAmount);
   output.textContent=`${data.discountPercent}% discount applied. Preview total: ₹${Number(data.finalAmount).toFixed(2)}.`;
  }catch(e){output.textContent=e.message}
 });
});
