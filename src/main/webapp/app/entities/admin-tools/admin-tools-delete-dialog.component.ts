import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { AdminTools } from './admin-tools.model';
import { AdminToolsPopupService } from './admin-tools-popup.service';
import { AdminToolsService } from './admin-tools.service';

@Component({
    selector: 'jhi-admin-tools-delete-dialog',
    templateUrl: './admin-tools-delete-dialog.component.html'
})
export class AdminToolsDeleteDialogComponent {

    adminTools: AdminTools;

    constructor(
        private adminToolsService: AdminToolsService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adminToolsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'adminToolsListModification',
                content: 'Deleted an adminTools'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('wBoardApp.adminTools.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-admin-tools-delete-popup',
    template: ''
})
export class AdminToolsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adminToolsPopupService: AdminToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.adminToolsPopupService
                .open(AdminToolsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
