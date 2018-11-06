import { browser, ExpectedConditions as ec } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NetLinkComponentsPage, NetLinkUpdatePage } from './net-link.page-object';

describe('NetLink e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let netLinkUpdatePage: NetLinkUpdatePage;
    let netLinkComponentsPage: NetLinkComponentsPage;

    beforeAll(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load NetLinks', async () => {
        await navBarPage.goToEntity('net-link');
        netLinkComponentsPage = new NetLinkComponentsPage();
        expect(await netLinkComponentsPage.getTitle()).toMatch(/insightApp.netLink.home.title/);
    });

    it('should load create NetLink page', async () => {
        await netLinkComponentsPage.clickOnCreateButton();
        netLinkUpdatePage = new NetLinkUpdatePage();
        expect(await netLinkUpdatePage.getPageTitle()).toMatch(/insightApp.netLink.home.createOrEditLabel/);
        await netLinkUpdatePage.cancel();
    });

    it('should create and save NetLinks', async () => {
        await netLinkComponentsPage.clickOnCreateButton();
        await netLinkUpdatePage.setDescriptionInput('description');
        expect(await netLinkUpdatePage.getDescriptionInput()).toMatch('description');
        await netLinkUpdatePage.setNomInput('nom');
        expect(await netLinkUpdatePage.getNomInput()).toMatch('nom');
        await netLinkUpdatePage.setTypeInput('type');
        expect(await netLinkUpdatePage.getTypeInput()).toMatch('type');
        await netLinkUpdatePage.setLevelInput('level');
        expect(await netLinkUpdatePage.getLevelInput()).toMatch('level');
        await netLinkUpdatePage.save();
        expect(await netLinkUpdatePage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(async () => {
        await navBarPage.autoSignOut();
    });
});
