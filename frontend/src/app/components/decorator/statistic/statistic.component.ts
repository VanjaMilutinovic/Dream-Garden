import { Component } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.css']
})
export class StatisticComponent {
  barChart: any = [];
  pieChart: any = [];
  histogramChart: any = [];

  constructor() { }

  ngOnInit() {
    this.barChart = new Chart('barChart', {
      type: 'bar',
      data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [
          {
            label: '# of Votes',
            data: [12, 19, 3, 5, 2, 3],
            borderWidth: 1,
          },
        ],
      },
      options: {
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Chart.js Bar Chart'
          }
        },
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });



    this.pieChart = new Chart('pieChart', {
      type: 'pie',
      data: {
        labels: ['Red', 'Orange', 'Yellow', 'Green', 'Blue'],
        datasets: [
          {
            label: 'Dataset 1',
            data: [1, 2, 3, 4, 5]
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Chart.js Pie Chart'
          }
        }
      },
    });

    this.histogramChart = new Chart('histogramChart', {
      type: 'bar',
      data: {
        labels: ['Pon', 'Uto','Sre', 'Cet'],
        datasets: [{
          label: 'Prosecan broj poslova',
          data: [
            6,17,28,15
          ],
          backgroundColor: "red",
          borderColor: "red",
          borderWidth: 1,
          barPercentage: 1,
          categoryPercentage: 1,
          borderRadius: 5,
        }]
      },
      options: {
        plugins: {
          legend: {
            position: 'top',
          },
          title: {
            display: true,
            text: 'Chart.js Histogram Chart'
          }
        },
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });

  }

}
