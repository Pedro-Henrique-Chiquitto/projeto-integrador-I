let day = document.querySelector('.day');
        let date = document.querySelector('.date');
        let year = document.querySelector('.year');
        let month = document.querySelector('.month');
        let prevButton = document.getElementById('prevDay');
        let nextButton = document.getElementById('nextDay');

        let lang = navigator.language;
        let today = new Date();
        let currentDate = new Date(); // Começa na data atual

        function updateDate() {
            date.innerText = currentDate.getDate();
            year.innerText = currentDate.getFullYear();
            month.innerText = currentDate.toLocaleDateString(lang, { month: 'long' });
            day.innerText = currentDate.toLocaleDateString(lang, { weekday: 'long' });

            // Desabilitar botão "Voltar" se a data for hoje
            prevButton.disabled = currentDate <= today;

            // Desabilitar botão "Avançar" se for 7 dias depois de hoje
            let maxDate = new Date(today);
            maxDate.setDate(today.getDate() + 7);
            nextButton.disabled = currentDate >= maxDate;
        }

        function changeDate(direction) {
            let newDate = new Date(currentDate);
            newDate.setDate(currentDate.getDate() + direction);

            // Verifica se a nova data está dentro do intervalo permitido
            let minDate = new Date(today);
            let maxDate = new Date(today);
            maxDate.setDate(today.getDate() + 7);

            if (newDate >= minDate && newDate <= maxDate) {
                currentDate = newDate;
                updateDate();
            }
        }

        window.onload = function() {
            updateDate(); // Inicializa com a data atual
        };