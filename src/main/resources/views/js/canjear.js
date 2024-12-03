function canjearPuntos(puntos, nombreOferta) {
    //alert(`Has canjeado ${puntos} puntos`);
   fetch('/puntos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ nombreOferta })
        })
}