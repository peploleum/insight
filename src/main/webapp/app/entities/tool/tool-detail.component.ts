import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITool } from 'app/shared/model/tool.model';

@Component({
    selector: 'jhi-tool-detail',
    templateUrl: './tool-detail.component.html'
})
export class ToolDetailComponent implements OnInit {
    tool: ITool;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ tool }) => {
            this.tool = tool;
        });
    }

    previousState() {
        window.history.back();
    }
}
