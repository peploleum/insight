import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, AccountService, Account } from 'app/core';
import { ProfileService } from '../layouts/profiles/profile.service';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss'],
    styles: [':host { flex-grow: 1}']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    applicationName: string;

    constructor(
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private profileService: ProfileService
    ) {}

    ngOnInit() {
        this.accountService.identity().then(account => {
            this.account = account;
        });
        this.profileService.getProfileInfo().then(profileInfo => {
            this.applicationName = profileInfo.reachEnabled ? 'Reach' : profileInfo.geniusEnabled ? 'Genius' : 'Insight';
        });
        this.registerAuthenticationSuccess();
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.accountService.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
