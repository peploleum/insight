import { browser, ExpectedConditions as ec, protractor } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ReportComponentsPage, ReportUpdatePage } from './report.page-object';

describe('Report e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let reportUpdatePage: ReportUpdatePage;
    let reportComponentsPage: ReportComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Reports', async () => {
        await navBarPage.goToEntity('report');
        reportComponentsPage = new ReportComponentsPage();
        expect(await reportComponentsPage.getTitle()).toMatch(/insightApp.report.home.title/);
    });

    it('should load create Report page', async () => {
        await reportComponentsPage.clickOnCreateButton();
        reportUpdatePage = new ReportUpdatePage();
        expect(await reportUpdatePage.getPageTitle()).toMatch(/insightApp.report.home.createOrEditLabel/);
        await reportUpdatePage.cancel();
    });

    it('should create and save Reports', async () => {
        await reportComponentsPage.clickOnCreateButton();
        await reportUpdatePage.setNomInput('nom');
        expect(await reportUpdatePage.getNomInput()).toMatch('nom');
        await reportUpdatePage.setTypeInput('type');
        expect(await reportUpdatePage.getTypeInput()).toMatch('type');
        await reportUpdatePage.setLibelleInput('libelle');
        expect(await reportUpdatePage.getLibelleInput()).toMatch('libelle');
        await reportUpdatePage.setObjetsReferencesInput('objetsReferences');
        expect(await reportUpdatePage.getObjetsReferencesInput()).toMatch('objetsReferences');
        await reportUpdatePage.setDatePublicationInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
        expect(await reportUpdatePage.getDatePublicationInput()).toContain('2001-01-01T02:30');
        await reportUpdatePage.linkOfSelectLastOption();
        await reportUpdatePage.save();
        expect(await reportUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
