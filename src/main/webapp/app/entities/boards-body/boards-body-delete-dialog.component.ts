import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AlertService, EventManager } from 'ng-jhipster';

import { BoardsBody } from './boards-body.model';
import { BoardsBodyPopupService } from './boards-body-popup.service';
import { BoardsBodyService } from './boards-body.service';

@Component({
    selector: 'jhi-boards-body-delete-dialog',
    templateUrl: './boards-body-delete-dialog.component.html'
})
export class BoardsBodyDeleteDialogComponent {

    boardsBody: BoardsBody;

    constructor(
        private boardsBodyService: BoardsBodyService,
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boardsBodyService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'boardsBodyListModification',
                content: 'Deleted an boardsBody'
            });
            this.activeModal.dismiss(true);
        });
        this.alertService.success('wBoardApp.boardsBody.deleted', { param : id }, null);
    }
}

@Component({
    selector: 'jhi-boards-body-delete-popup',
    template: ''
})
export class BoardsBodyDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boardsBodyPopupService: BoardsBodyPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.boardsBodyPopupService
                .open(BoardsBodyDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
