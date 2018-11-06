import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ThreatActorComponentsPage, ThreatActorUpdatePage } from './threat-actor.page-object';

describe('ThreatActor e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let threatActorUpdatePage: ThreatActorUpdatePage;
    let threatActorComponentsPage: ThreatActorComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load ThreatActors', async () => {
        await navBarPage.goToEntity('threat-actor');
        threatActorComponentsPage = new ThreatActorComponentsPage();
        expect(await threatActorComponentsPage.getTitle()).toMatch(/insightApp.threatActor.home.title/);
    });

    it('should load create ThreatActor page', async () => {
        await threatActorComponentsPage.clickOnCreateButton();
        threatActorUpdatePage = new ThreatActorUpdatePage();
        expect(await threatActorUpdatePage.getPageTitle()).toMatch(/insightApp.threatActor.home.createOrEditLabel/);
        await threatActorUpdatePage.cancel();
    });

    it('should create and save ThreatActors', async () => {
        await threatActorComponentsPage.clickOnCreateButton();
        await threatActorUpdatePage.setNomInput('nom');
        expect(await threatActorUpdatePage.getNomInput()).toMatch('nom');
        await threatActorUpdatePage.setTypeInput('type');
        expect(await threatActorUpdatePage.getTypeInput()).toMatch('type');
        await threatActorUpdatePage.setLibelleInput('libelle');
        expect(await threatActorUpdatePage.getLibelleInput()).toMatch('libelle');
        await threatActorUpdatePage.setSpecificationInput('specification');
        expect(await threatActorUpdatePage.getSpecificationInput()).toMatch('specification');
        await threatActorUpdatePage.setRoleInput('role');
        expect(await threatActorUpdatePage.getRoleInput()).toMatch('role');
        await threatActorUpdatePage.isUsesThreatActorToMalwareSelectLastOption();
        await threatActorUpdatePage.linkOfSelectLastOption();
        await threatActorUpdatePage.save();
        expect(await threatActorUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
