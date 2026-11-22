const ADMIN_API='/api';
document.addEventListener('DOMContentLoaded',async()=>{
 const status=document.getElementById('adminStatus'), target=document.getElementById('adminStats');
 try{const res=await fetch(`${ADMIN_API}/admin/summary`);if(!res.ok)throw new Error('Admin API unavailable. Check backend integration.');
 const data=await res.json();target.innerHTML='';
 Object.entries(data).forEach(([k,v])=>{const card=document.createElement('article');card.className='room';
 const title=document.createElement('h3');title.textContent=k.replace(/([A-Z])/g,' $1');
 const value=document.createElement('p');value.className='price';value.textContent=v;card.append(title,value);target.append(card)});
 status.textContent='Dashboard loaded.';
 }catch(e){status.textContent=e.message}
});
