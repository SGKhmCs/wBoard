import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { ReaderTools } from './reader-tools.model';
import { ReaderToolsPopupService } from './reader-tools-popup.service';
import { ReaderToolsService } from './reader-tools.service';

@Component({
    selector: 'jhi-reader-tools-delete-dialog',
    templateUrl: './reader-tools-delete-dialog.component.html'
})
export class ReaderToolsDeleteDialogComponent {

    readerTools: ReaderTools;

    constructor(
        private readerToolsService: ReaderToolsService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.readerToolsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'readerToolsListModification',
                content: 'Deleted an readerTools'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('wBoardApp.readerTools.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-reader-tools-delete-popup',
    template: ''
})
export class ReaderToolsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private readerToolsPopupService: ReaderToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.readerToolsPopupService
                .open(ReaderToolsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
