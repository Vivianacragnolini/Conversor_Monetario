document.addEventListener("DOMContentLoaded", () => {
    const monedaOrigenSelect = document.getElementById("moneda-origen");
    const monedaDestinoSelect = document.getElementById("moneda-destino");

    // Fetch list of currencies
    fetch("/api/monedas")
        .then(response => response.json())
        .then(monedas => {
            monedas.forEach(moneda => {
                let optionOrigen = document.createElement("option");
                optionOrigen.value = moneda.codigo;
                optionOrigen.textContent = moneda.nombre;
                monedaOrigenSelect.appendChild(optionOrigen);

                let optionDestino = document.createElement("option");
                optionDestino.value = moneda.codigo;
                optionDestino.textContent = moneda.nombre;
                monedaDestinoSelect.appendChild(optionDestino);
            });
        });

    // Handle form submission
    const form = document.getElementById("form-conversor");
    form.addEventListener("submit", event => {
        event.preventDefault();

        const monedaOrigen = monedaOrigenSelect.value;
        const monedaDestino = monedaDestinoSelect.value;
        const cantidad = document.getElementById("cantidad").value;

        fetch(`/api/convertir?origen=${monedaOrigen}&destino=${monedaDestino}&cantidad=${cantidad}`)
            .then(response => response.json())
            .then(resultado => {
                document.getElementById("resultado").textContent = `Resultado: ${resultado}`;
            });
    });
});
