import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'ng-jhipster';

import { Board } from './board.model';
import { BoardPopupService } from './board-popup.service';
import { BoardService } from './board.service';

@Component({
    selector: 'jhi-board-delete-dialog',
    templateUrl: './board-delete-dialog.component.html'
})
export class BoardDeleteDialogComponent {

    board: Board;

    constructor(
        private boardService: BoardService,
        public activeModal: NgbActiveModal,
        private eventManager: EventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.boardService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'boardListModification',
                content: 'Deleted an board'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-board-delete-popup',
    template: ''
})
export class BoardDeletePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private boardPopupService: BoardPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.modalRef = this.boardPopupService
                .open(BoardDeleteDialogComponent, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
