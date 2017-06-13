import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { WriterTools } from './writer-tools.model';
import { WriterToolsPopupService } from './writer-tools-popup.service';
import { WriterToolsService } from './writer-tools.service';

@Component({
    selector: 'jhi-writer-tools-delete-dialog',
    templateUrl: './writer-tools-delete-dialog.component.html'
})
export class WriterToolsDeleteDialogComponent {

    writerTools: WriterTools;

    constructor(
        private writerToolsService: WriterToolsService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.writerToolsService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'writerToolsListModification',
                content: 'Deleted an writerTools'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('wBoardApp.writerTools.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-writer-tools-delete-popup',
    template: ''
})
export class WriterToolsDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private writerToolsPopupService: WriterToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.writerToolsPopupService
                .open(WriterToolsDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
