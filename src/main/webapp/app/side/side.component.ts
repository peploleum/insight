/**
 * Created by gFolgoas on 01/02/2019.
 */
import { Component, OnDestroy, OnInit } from '@angular/core';
import { SideInterface } from './side.abstract';
import { ActivatedRoute } from '@angular/router';
import { ProfileService } from '../layouts/profiles/profile.service';

@Component({
    selector: 'ins-side',
    templateUrl: './side.component.html'
})
export class SideComponent extends SideInterface implements OnInit, OnDestroy {
    _sideElement: string;
    target: string;
    applicationName = 'Insight';

    constructor(private activatedRoute: ActivatedRoute, private profileService: ProfileService) {
        super();
        this.target = this.activatedRoute.snapshot.data['target'];
        this._sideElement = this.target === 'map' ? 'EVENT_THREAD' : this.target === 'network' ? 'EVENT_THREAD' : '';
    }

    ngOnInit() {
        this.profileService.getProfileInfo().then(profileInfo => {
            this.applicationName = profileInfo.reachEnabled ? 'Reach' : profileInfo.geniusEnabled ? 'Genius' : 'Insight';
        });
    }

    ngOnDestroy() {}

    changeSideElement(value: string) {
        this._sideElement = value;
    }
}
