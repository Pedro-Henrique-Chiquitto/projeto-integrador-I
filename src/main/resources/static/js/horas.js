function gerarHorarios(selectHoras, selectMinutos) {
    for (let h = 0; h < 24; h++) {
        let hora = h.toString().padStart(2, '0');
        let option = document.createElement("option");
        option.value = hora;
        option.textContent = hora;
        selectHoras.appendChild(option);
    }
    
    for (let m = 0; m < 60; m += 5) {
        let minuto = m.toString().padStart(2, '0');
        let option = document.createElement("option");
        option.value = minuto;
        option.textContent = minuto;
        selectMinutos.appendChild(option);
    }
}

function atualizarHorario() {
    let horaInicio = document.getElementById("horasInicio").value;
    let minutoInicio = document.getElementById("minutosInicio").value;
    document.getElementById("horarioSelecionado").textContent = "Horário selecionado: " + horaInicio + ":" + minutoInicio + " - --:--";
}

function validarHorario() {
    let horaInicio = parseInt(document.getElementById("horasInicio").value);
    let minutoInicio = parseInt(document.getElementById("minutosInicio").value);
    let horaFim = parseInt(document.getElementById("horasFim").value);
    let minutoFim = parseInt(document.getElementById("minutosFim").value);
    
    if (isNaN(horaFim) || isNaN(minutoFim)) {
        return;
    }
    
    if (horaFim < horaInicio || (horaFim === horaInicio && minutoFim < minutoInicio)) {
        alert("O horário de fim não pode ser menor que o horário de início! Por favor, selecione novamente.");
        document.getElementById("horasFim").value = "";
        document.getElementById("minutosFim").value = "";
        return;
    }
    
    document.getElementById("horarioSelecionado").textContent = "Horário selecionado: " + 
        horaInicio.toString().padStart(2, '0') + ":" + minutoInicio.toString().padStart(2, '0') + " - " + 
        horaFim.toString().padStart(2, '0') + ":" + minutoFim.toString().padStart(2, '0');
}

gerarHorarios(document.getElementById("horasInicio"), document.getElementById("minutosInicio"));
gerarHorarios(document.getElementById("horasFim"), document.getElementById("minutosFim"));