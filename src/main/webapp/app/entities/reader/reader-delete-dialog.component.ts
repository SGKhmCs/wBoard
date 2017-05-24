import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Reader } from './reader.model';
import { ReaderPopupService } from './reader-popup.service';
import { ReaderService } from './reader.service';

@Component({
    selector: 'jhi-reader-delete-dialog',
    templateUrl: './reader-delete-dialog.component.html'
})
export class ReaderDeleteDialogComponent {

    reader: Reader;

    constructor(
        private readerService: ReaderService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.readerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'readerListModification',
                content: 'Deleted an reader'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-reader-delete-popup',
    template: ''
})
export class ReaderDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private readerPopupService: ReaderPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.readerPopupService
                .open(ReaderDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
