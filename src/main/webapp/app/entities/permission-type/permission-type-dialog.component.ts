import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { PermissionType } from './permission-type.model';
import { PermissionTypePopupService } from './permission-type-popup.service';
import { PermissionTypeService } from './permission-type.service';

@Component({
    selector: 'jhi-permission-type-dialog',
    templateUrl: './permission-type-dialog.component.html'
})
export class PermissionTypeDialogComponent implements OnInit {

    permissionType: PermissionType;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private permissionTypeService: PermissionTypeService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.permissionType.id !== undefined) {
            this.subscribeToSaveResponse(
                this.permissionTypeService.update(this.permissionType));
        } else {
            this.subscribeToSaveResponse(
                this.permissionTypeService.create(this.permissionType));
        }
    }

    private subscribeToSaveResponse(result: Observable<PermissionType>) {
        result.subscribe((res: PermissionType) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: PermissionType) {
        this.eventManager.broadcast({ name: 'permissionTypeListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-permission-type-popup',
    template: ''
})
export class PermissionTypePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private permissionTypePopupService: PermissionTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.permissionTypePopupService
                    .open(PermissionTypeDialogComponent, params['id']);
            } else {
                this.modalRef = this.permissionTypePopupService
                    .open(PermissionTypeDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
