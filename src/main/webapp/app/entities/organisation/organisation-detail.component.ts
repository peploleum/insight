import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrganisation } from 'app/shared/model/organisation.model';

@Component({
    selector: 'jhi-organisation-detail',
    templateUrl: './organisation-detail.component.html'
})
export class OrganisationDetailComponent implements OnInit {
    organisation: IOrganisation;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organisation }) => {
            this.organisation = organisation;
        });
    }

    previousState() {
        window.history.back();
    }
}
