import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IOrganisation } from 'app/shared/model/organisation.model';

@Component({
    selector: 'ins-organisation-detail',
    templateUrl: './organisation-detail.component.html'
})
export class OrganisationDetailComponent implements OnInit {
    organisation: IOrganisation;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ organisation }) => {
            this.organisation = organisation;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
