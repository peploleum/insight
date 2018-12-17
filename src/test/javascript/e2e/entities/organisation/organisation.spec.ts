/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { OrganisationComponentsPage, OrganisationDeleteDialog, OrganisationUpdatePage } from './organisation.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Organisation e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let organisationUpdatePage: OrganisationUpdatePage;
    let organisationComponentsPage: OrganisationComponentsPage;
    let organisationDeleteDialog: OrganisationDeleteDialog;
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

    it('should load Organisations', async () => {
        await navBarPage.goToEntity('organisation');
        organisationComponentsPage = new OrganisationComponentsPage();
        expect(await organisationComponentsPage.getTitle()).to.eq('insightApp.organisation.home.title');
    });

    it('should load create Organisation page', async () => {
        await organisationComponentsPage.clickOnCreateButton();
        organisationUpdatePage = new OrganisationUpdatePage();
        expect(await organisationUpdatePage.getPageTitle()).to.eq('insightApp.organisation.home.createOrEditLabel');
        await organisationUpdatePage.cancel();
    });

    it('should create and save Organisations', async () => {
        const nbButtonsBeforeCreate = await organisationComponentsPage.countDeleteButtons();

        await organisationComponentsPage.clickOnCreateButton();
        await promise.all([
            organisationUpdatePage.setOrganisationNameInput('organisationName'),
            organisationUpdatePage.setOrganisationDescrptionInput('organisationDescrption'),
            organisationUpdatePage.organisationSizeSelectLastOption(),
            organisationUpdatePage.setOrganisationCoordinatesInput('organisationCoordinates'),
            organisationUpdatePage.setOrganisationImageInput(absolutePath),
            organisationUpdatePage.setOrganisationSymbolInput('organisationSymbol')
        ]);
        expect(await organisationUpdatePage.getOrganisationNameInput()).to.eq('organisationName');
        expect(await organisationUpdatePage.getOrganisationDescrptionInput()).to.eq('organisationDescrption');
        expect(await organisationUpdatePage.getOrganisationCoordinatesInput()).to.eq('organisationCoordinates');
        expect(await organisationUpdatePage.getOrganisationImageInput()).to.endsWith(fileNameToUpload);
        expect(await organisationUpdatePage.getOrganisationSymbolInput()).to.eq('organisationSymbol');
        await organisationUpdatePage.save();
        expect(await organisationUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await organisationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Organisation', async () => {
        const nbButtonsBeforeDelete = await organisationComponentsPage.countDeleteButtons();
        await organisationComponentsPage.clickOnLastDeleteButton();

        organisationDeleteDialog = new OrganisationDeleteDialog();
        expect(await organisationDeleteDialog.getDialogTitle()).to.eq('insightApp.organisation.delete.question');
        await organisationDeleteDialog.clickOnConfirmButton();

        expect(await organisationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
