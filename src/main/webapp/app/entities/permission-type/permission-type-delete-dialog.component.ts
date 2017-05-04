import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, JhiLanguageService } from 'ng-jhipster';

import { PermissionType } from './permission-type.model';
import { PermissionTypePopupService } from './permission-type-popup.service';
import { PermissionTypeService } from './permission-type.service';

@Component({
    selector: 'jhi-permission-type-delete-dialog',
    templateUrl: './permission-type-delete-dialog.component.html'
})
export class PermissionTypeDeleteDialogComponent {

    permissionType: PermissionType;

    constructor(
        private jhiLanguageService: JhiLanguageService,
        private permissionTypeService: PermissionTypeService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['permissionType']);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.permissionTypeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'permissionTypeListModification',
                content: 'Deleted an permissionType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-permission-type-delete-popup',
    template: ''
})
export class PermissionTypeDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private permissionTypePopupService: PermissionTypePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.permissionTypePopupService
                .open(PermissionTypeDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
