import { Component } from '@angular/core';
import Chart from 'chart.js/auto';
import { firstValueFrom } from 'rxjs';
import { User } from 'src/app/models/user.model';
import { Worker } from 'src/app/models/worker.model';
import { StatisticService } from 'src/app/services/statistic/statistic.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-statistic',
  templateUrl: './statistic.component.html',
  styleUrls: ['./statistic.component.css']
})
export class StatisticComponent {
  barChart: any = [];
  pieChart: any = [];
  histogramChart: any = [];

  error_msg: string = "";

  constructor(
    private userService: UserService,
    private statisticService: StatisticService
  ) {
    try {
      const user = JSON.parse(localStorage.getItem("user") ?? "") as User
    } catch (error) {
      console.warn("Nekako se u localStorage upisao nevalidan User");
      window.location.href = "/login";
      return;
    }
  }

  async ngOnInit() {

    let workerId: number;
    let companyId: number;

    try {

      workerId = (JSON.parse(localStorage.getItem("user") ?? "") as User).userId;
      const worker = await firstValueFrom(this.userService.getWorker(workerId)) as Worker;
      companyId = worker.companyId.companyId

    } catch (error) {
      console.warn(error);
      this.error_msg = "An error occured. Please refresh the page. 🙏"
      return;
    }

    let barChartData = { labels: [], data: [] };
    let pieChartData = { labels: [], data: [] };
    let histogramChartData = { labels: [], data: [] };

    try {
      //Bar chart data 
      let resp = await firstValueFrom(this.statisticService.getByWorker(workerId));
      barChartData.labels = resp.map((x: any) => x.variableName);
      barChartData.data = resp.map((x: any) => x.variableCount);

      //Pie chart data
      resp = await firstValueFrom(this.statisticService.getByCompany(companyId));
      pieChartData.labels = resp.map((x: any) => x.variableName);
      pieChartData.data = resp.map((x: any) => x.variableCount);

      //Histogram chart data
      resp = await firstValueFrom(this.statisticService.getByDay());
      histogramChartData.labels = resp.map((x: any) => x.variableName);
      histogramChartData.data = resp.map((x: any) => x.variableCount);

    } catch (error) {
      console.warn(error);
      this.error_msg = "An error occured while the statistics were generating. Please try again later 🙏"
      return;
    }

    this.barChart = new Chart('barChart', {
      type: 'bar',
      data: {
        labels: barChartData.labels,
        datasets: [
          {
            label: '# of jobs',
            data: barChartData.data,
            borderWidth: 1,
            backgroundColor: "#f06292",
            borderRadius: 5
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
            text: 'Broj poslova po mesecima'
          }
        },
        scales: {
          y: {
            ticks: {
              precision: 0
            },
            beginAtZero: true,
          },
        },
      },
    });

    this.pieChart = new Chart('pieChart', {
      type: 'pie',
      data: {
        labels: pieChartData.labels,
        datasets: [
          {
            data: pieChartData.data
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
            text: 'Broj poslova po zaposlenima u kompaniji'
          }
        }
      },
    });

    this.histogramChart = new Chart('histogramChart', {
      type: 'bar',
      data: {
        labels: histogramChartData.labels,
        datasets: [{
          label: 'Prosečan broj poslova po danima u nedelji',
          data: histogramChartData.data,
          backgroundColor: "#f06292",
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
