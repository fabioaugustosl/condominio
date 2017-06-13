function subirTela(){
	$('html,body').animate({scrollTop: 0},'slow');
}

function phoneMaskHandler(event) {  
    var target, phone, element;     
    target = (event.currentTarget) ? event.currentTarget : event.srcElement;    
    phone = target.value.replace(/\D/g, '');    
    element = $(target);    
    element.unmask();   
    if(phone.length > 10) {
        element.mask("(99) 99999-999?9");
    } else {
        element.mask("(99) 9999-9999?9");
    }
}
