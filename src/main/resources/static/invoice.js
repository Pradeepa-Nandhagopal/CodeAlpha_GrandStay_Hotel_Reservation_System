document.addEventListener('DOMContentLoaded',async()=>{
 const target=document.getElementById('invoiceContent'),id=localStorage.getItem('bookingId');
 if(!id){target.textContent='No booking ID found. Save the booking ID after a successful reservation.';return}
 try{const res=await fetch('/api/bookings');const bookings=await res.json();if(!res.ok)throw new Error('Could not load bookings');
  const b=bookings.find(x=>String(x.id)===String(id));if(!b)throw new Error('Booking not found');
  const rows=[['Booking ID',b.id],['Guest',b.customer?.name||'Guest'],['Room',b.room?.roomType||'Room'],['Check-in',b.checkIn],['Check-out',b.checkOut],['Total',`₹${Number(b.totalAmount).toLocaleString('en-IN')}`],['Status',b.status]];
  target.innerHTML='';rows.forEach(([k,v])=>{const line=document.createElement('div');line.className='line';const a=document.createElement('b');a.textContent=k;const s=document.createElement('span');s.textContent=v;line.append(a,s);target.append(line)});
 }catch(e){target.textContent=e.message}
});
