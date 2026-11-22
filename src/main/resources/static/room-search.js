const SEARCH_API='/api';
document.addEventListener('DOMContentLoaded',()=>{
 const form=document.getElementById('roomSearchForm'), output=document.getElementById('roomSearchResults');
 if(!form||!output)return;
 form.addEventListener('submit',async e=>{
  e.preventDefault();const params=new URLSearchParams(new FormData(form));output.textContent='Searching...';
  try{const res=await fetch(`${SEARCH_API}/rooms/search?${params}`);const data=await res.json();
   if(!res.ok)throw new Error(data.error||'Search failed');
   output.innerHTML='';
   if(!data.length){output.textContent='No available rooms match your filters.';return}
   data.forEach(room=>{const card=document.createElement('article');card.className='room';
    const heading=document.createElement('h3');heading.textContent=room.roomType;
    const price=document.createElement('p');price.textContent=`₹${room.price} per night`;
    const choose=document.createElement('button');choose.textContent='Choose room';
    choose.addEventListener('click',()=>{localStorage.setItem('selectedRoomId',room.id);localStorage.setItem('selectedRoom',room.roomType);location.href='booking.html'});
    card.append(heading,price,choose);output.append(card);
   });
  }catch(err){output.textContent=err.message}
 });
});
