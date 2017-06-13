import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { OwnerTools } from './owner-tools.model';
import { OwnerToolsPopupService } from './owner-tools-popup.service';
import { OwnerToolsService } from './owner-tools.service';

@Component({
    selector: 'jhi-owner-tools-delete-dialog',
    templateUrl: './owner-tools-delete-dialog.component.html'
})
export class OwnerToolsDeleteDialogComponent {

    ownerTools: OwnerTools;

    constructor(
        private ownerToolsService: OwnerToolsService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.ownerToolsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'ownerToolsListModification',
                content: 'Deleted an ownerTools'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('wBoardApp.ownerTools.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-owner-tools-delete-popup',
    template: ''
})
export class OwnerToolsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ownerToolsPopupService: OwnerToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.ownerToolsPopupService
                .open(OwnerToolsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
