/**
 * Created by gFolgoas on 31/01/2019.
 */
import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { SideInterface } from '../../shared/side/side.abstract';
import { MapService } from '../map.service';
import * as Chart from 'chart.js';
import { EventThreadResultSet } from '../../shared/util/map-utils';
import { debounceTime } from 'rxjs/internal/operators';
import { IRawData } from '../../shared/model/raw-data.model';

@Component({
    selector: 'ins-quick-stats',
    templateUrl: './quick-stats.component.html'
})
export class QuickStatsComponent extends SideInterface implements OnInit, AfterViewInit, OnDestroy {
    chart: Chart;
    @ViewChild('canvas', { read: ElementRef }) canvas: ElementRef;
    dataSet: { labels: string[]; data: any[]; fillColors: string[]; strokeColors: string[] } = {
        labels: [],
        data: [],
        fillColors: [],
        strokeColors: []
    };
    countProperty = 'rawDataType';

    constructor(protected ms: MapService) {
        super();
    }

    ngOnInit() {}

    changeAnalysedProperty(property: string) {
        this.countProperty = property;
        this.prepareDataSet(this.ms.rawDataStream.getValue(), this.countProperty);
        this.updateChart();
    }

    ngAfterViewInit() {
        this.ms.rawDataStream.pipe(debounceTime(100)).subscribe((data: EventThreadResultSet) => {
            this.prepareDataSet(data, this.countProperty);
            if (this.dataSet.labels.length && this.dataSet.data.length) {
                if (!this.chart) {
                    this.initChart();
                } else {
                    this.updateChart();
                }
            }
        });
    }

    updateChart() {
        this.chart.data = {
            labels: this.dataSet.labels,
            datasets: [
                {
                    label: `RawData: ${this.countProperty}`,
                    data: this.dataSet.data,
                    backgroundColor: this.dataSet.fillColors,
                    borderColor: this.dataSet.strokeColors,
                    borderWidth: 1
                }
            ]
        };
        this.chart.update();
    }

    prepareDataSet(data: EventThreadResultSet, targetFieldCount: string) {
        const temp = {};
        data.data.forEach((item: IRawData) => {
            if (temp[item[targetFieldCount]]) {
                temp[item[targetFieldCount]]++;
            } else {
                temp[item[targetFieldCount]] = 1;
            }
        });
        this.dataSet.labels = Object.keys(temp);
        this.dataSet.data = Object.keys(temp).map(key => {
            return temp[key];
        });
        const randomColors: string[] = Object.keys(temp).map(key => this.randomRgb());
        this.dataSet.fillColors = randomColors.map(color => this.transformToRgba(color, true));
        this.dataSet.strokeColors = randomColors.map(color => this.transformToRgba(color, false));
    }

    initChart() {
        this.chart = new Chart(this.getCanvasContext(), {
            type: 'horizontalBar',
            data: {
                labels: this.dataSet.labels,
                datasets: [
                    {
                        label: `RawData: ${this.countProperty}`,
                        data: this.dataSet.data,
                        backgroundColor: this.dataSet.fillColors,
                        borderColor: this.dataSet.strokeColors,
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    display: false
                },
                scales: {
                    xAxes: [
                        {
                            display: true,
                            ticks: {
                                beginAtZero: true,
                                fontColor: 'white',
                                stepSize: 1
                            }
                        }
                    ],
                    yAxes: [
                        {
                            maxBarThickness: 15,
                            display: true,
                            gridLines: {
                                display: false
                            },
                            ticks: {
                                fontColor: 'white'
                            }
                        }
                    ]
                }
            }
        });
    }

    ngOnDestroy() {}

    getCanvasContext() {
        return (<HTMLCanvasElement>this.canvas.nativeElement).getContext('2d');
    }

    randomRgb(): string {
        const o = Math.round;
        const r = Math.random;
        const s = 255;
        return 'rgb(' + o(r() * s) + ',' + o(r() * s) + ',' + o(r() * s) + ')';
    }

    transformToRgba(rgb: string, fillColor: boolean) {
        const numbers: RegExpMatchArray = rgb.match(/(\d+)/g);
        const opacity = fillColor ? 0.2 : 1;
        return 'rgba(' + numbers[0] + ',' + numbers[1] + ',' + numbers[2] + ',' + opacity + ')';
    }
}
