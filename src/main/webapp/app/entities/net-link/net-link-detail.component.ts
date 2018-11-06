import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INetLink } from 'app/shared/model/net-link.model';

@Component({
    selector: 'jhi-net-link-detail',
    templateUrl: './net-link-detail.component.html'
})
export class NetLinkDetailComponent implements OnInit {
    netLink: INetLink;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ netLink }) => {
            this.netLink = netLink;
        });
    }

    previousState() {
        window.history.back();
    }
}
