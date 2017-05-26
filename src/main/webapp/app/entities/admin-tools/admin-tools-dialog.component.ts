import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { AdminTools } from './admin-tools.model';
import { AdminToolsPopupService } from './admin-tools-popup.service';
import { AdminToolsService } from './admin-tools.service';
import { Admin, AdminService } from '../admin';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-admin-tools-dialog',
    templateUrl: './admin-tools-dialog.component.html'
})
export class AdminToolsDialogComponent implements OnInit {

    adminTools: AdminTools;
    authorities: any[];
    isSaving: boolean;

    admins: Admin[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private adminToolsService: AdminToolsService,
        private adminService: AdminService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.adminService
            .query({filter: 'admintools-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.adminTools.adminId) {
                    this.admins = res.json;
                } else {
                    this.adminService
                        .find(this.adminTools.adminId)
                        .subscribe((subRes: Admin) => {
                            this.admins = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.adminTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.adminToolsService.update(this.adminTools));
        } else {
            this.subscribeToSaveResponse(
                this.adminToolsService.create(this.adminTools));
        }
    }

    private subscribeToSaveResponse(result: Observable<AdminTools>) {
        result.subscribe((res: AdminTools) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: AdminTools) {
        this.eventManager.broadcast({ name: 'adminToolsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackAdminById(index: number, item: Admin) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-admin-tools-popup',
    template: ''
})
export class AdminToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adminToolsPopupService: AdminToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.adminToolsPopupService
                    .open(AdminToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.adminToolsPopupService
                    .open(AdminToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
