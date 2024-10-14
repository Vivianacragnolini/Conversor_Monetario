document.addEventListener("DOMContentLoaded", () => {
    const monedaOrigenSelect = document.getElementById("moneda-origen");
    const monedaDestinoSelect = document.getElementById("moneda-destino");

    // Fetch list of currencies
    fetch("/api/*")
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
        })
        .catch(error => {
            console.error("Error al cargar las monedas:", error);
            document.getElementById("resultado").textContent = "Error al cargar las monedas.";
        });

    // Handle form submission
    const form = document.getElementById("form-conversor");
    form.addEventListener("submit", event => {
        event.preventDefault();

        const monedaOrigen = monedaOrigenSelect.value;
        const monedaDestino = monedaDestinoSelect.value;
        const cantidad = document.getElementById("cantidad").value;

        if (!monedaOrigen || !monedaDestino || !cantidad || cantidad <= 0) {
            alert("Por favor, selecciona las monedas y una cantidad válida.");
            return;
        }

        fetch(`/api/pair?origen=${monedaOrigen}&destino=${monedaDestino}&cantidad=${cantidad}`)
            .then(response => response.json())
            .then(resultado => {
                document.getElementById("resultado").textContent = `Resultado: ${resultado}`;
            })
            .catch(error => {
                console.error("Error al realizar la conversión:", error);
                document.getElementById("resultado").textContent = "Error: No se pudo realizar la conversión.";
            });
    });
});
