import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Writer } from './writer.model';
import { WriterPopupService } from './writer-popup.service';
import { WriterService } from './writer.service';

@Component({
    selector: 'jhi-writer-delete-dialog',
    templateUrl: './writer-delete-dialog.component.html'
})
export class WriterDeleteDialogComponent {

    writer: Writer;

    constructor(
        private writerService: WriterService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.writerService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'writerListModification',
                content: 'Deleted an writer'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-writer-delete-popup',
    template: ''
})
export class WriterDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private writerPopupService: WriterPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.writerPopupService
                .open(WriterDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
