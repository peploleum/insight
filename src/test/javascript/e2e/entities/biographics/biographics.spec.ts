/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { BiographicsComponentsPage, BiographicsDeleteDialog, BiographicsUpdatePage } from './biographics.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Biographics e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let biographicsUpdatePage: BiographicsUpdatePage;
    let biographicsComponentsPage: BiographicsComponentsPage;
    let biographicsDeleteDialog: BiographicsDeleteDialog;
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Biographics', async () => {
        await navBarPage.goToEntity('biographics');
        biographicsComponentsPage = new BiographicsComponentsPage();
        expect(await biographicsComponentsPage.getTitle()).to.eq('insightApp.biographics.home.title');
    });

    it('should load create Biographics page', async () => {
        await biographicsComponentsPage.clickOnCreateButton();
        biographicsUpdatePage = new BiographicsUpdatePage();
        expect(await biographicsUpdatePage.getPageTitle()).to.eq('insightApp.biographics.home.createOrEditLabel');
        await biographicsUpdatePage.cancel();
    });

    it('should create and save Biographics', async () => {
        const nbButtonsBeforeCreate = await biographicsComponentsPage.countDeleteButtons();

        await biographicsComponentsPage.clickOnCreateButton();
        await promise.all([
            biographicsUpdatePage.setBiographicsFirstnameInput('biographicsFirstname'),
            biographicsUpdatePage.setBiographicsNameInput('biographicsName'),
            biographicsUpdatePage.setBiographicsAgeInput('5'),
            biographicsUpdatePage.biographicsGenderSelectLastOption(),
            biographicsUpdatePage.setBiographicsImageInput(absolutePath),
            biographicsUpdatePage.setBiographicsCoordinatesInput('biographicsCoordinates'),
            biographicsUpdatePage.setBiographicsSymbolInput('biographicsSymbol')
        ]);
        expect(await biographicsUpdatePage.getBiographicsFirstnameInput()).to.eq('biographicsFirstname');
        expect(await biographicsUpdatePage.getBiographicsNameInput()).to.eq('biographicsName');
        expect(await biographicsUpdatePage.getBiographicsAgeInput()).to.eq('5');
        expect(await biographicsUpdatePage.getBiographicsImageInput()).to.endsWith(fileNameToUpload);
        expect(await biographicsUpdatePage.getBiographicsCoordinatesInput()).to.eq('biographicsCoordinates');
        expect(await biographicsUpdatePage.getBiographicsSymbolInput()).to.eq('biographicsSymbol');
        await biographicsUpdatePage.save();
        expect(await biographicsUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await biographicsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Biographics', async () => {
        const nbButtonsBeforeDelete = await biographicsComponentsPage.countDeleteButtons();
        await biographicsComponentsPage.clickOnLastDeleteButton();

        biographicsDeleteDialog = new BiographicsDeleteDialog();
        expect(await biographicsDeleteDialog.getDialogTitle()).to.eq('insightApp.biographics.delete.question');
        await biographicsDeleteDialog.clickOnConfirmButton();

        expect(await biographicsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
