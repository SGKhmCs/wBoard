import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Admin } from './admin.model';
import { AdminPopupService } from './admin-popup.service';
import { AdminService } from './admin.service';

@Component({
    selector: 'jhi-admin-delete-dialog',
    templateUrl: './admin-delete-dialog.component.html'
})
export class AdminDeleteDialogComponent {

    admin: Admin;

    constructor(
        private adminService: AdminService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.adminService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'adminListModification',
                content: 'Deleted an admin'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-admin-delete-popup',
    template: ''
})
export class AdminDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private adminPopupService: AdminPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.adminPopupService
                .open(AdminDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
