# MyWeather

Trabalho de conclusão da disciplina persistencia de dados e networking: Weather APP - Professor: Wellington Cabral - Turma: Dev-Apps 2020.2

1. Finalizar o layout; <p style="color:blue"><b>DONE</b></p>
2. Ao clicar em “SEARCH”
○ Considerar as preferências do sistema:
	1. C° ou F° <b>DONE</b>
	2. Idioma <b>MISSING</b>
	3. Offline mode  <b>OPTIONAL</b>	
		● True: considerar os
		últimos dados salvos
		localmente;
		● False: Mostrar mensagem 
		ao usuário
			○ Caso não tenha resultado, mostrar a lista vazia.
3. Ao clicar em um item da lista, abrir a tela
Forecast. <b>DONE</b>

Forecast
1. Receber como parâmetro a cidade que foi selecionado pelo usuário na tela de Search; <b>DONE</b>
2. Para conseguir a lista da previsão do clima para a cidade, fazer a requisição para o serviço {OpenWeatherMapUrl}/forecast?id={cityId} <b>DONE</b>
3. Ao clicar no botão FAVORITE, salvar no banco de dados local a cidade. Se a cidade já existir no banco, você deve excluí-la para que ela deixe de ser favorita. <b>DONE</b>

Favorite
1. Listar todas as cidades favoritas diretamente do banco de dados local; <b>DONE</b>
2. Ao clicar no botão SEARCH, fazer consultas para filtrar os dados pelo nome da cidade ou pelo país; <b>DONE</b>
3. Ao clicar no botão DELETE, apagar o item do banco de dados; <b>DONE</b>


![Android Emulator - Pixel_XL_API_29_5554 2021-03-20 19-29-33](https://user-images.githubusercontent.com/61049676/111887364-6c0ee980-89b3-11eb-9021-38cf95320547.gif)





