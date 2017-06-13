import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService } from 'ng-jhipster';

import { OwnerTools } from './owner-tools.model';
import { OwnerToolsPopupService } from './owner-tools-popup.service';
import { OwnerToolsService } from './owner-tools.service';
import { User, UserService } from '../../shared';
import { Board, BoardService } from '../board';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-owner-tools-dialog',
    templateUrl: './owner-tools-dialog.component.html'
})
export class OwnerToolsDialogComponent implements OnInit {

    ownerTools: OwnerTools;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    boards: Board[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: AlertService,
        private ownerToolsService: OwnerToolsService,
        private userService: UserService,
        private boardService: BoardService,
        private eventManager: EventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query()
            .subscribe((res: ResponseWrapper) => { this.users = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.boardService
            .query({filter: 'ownertools-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.ownerTools.boardId) {
                    this.boards = res.json;
                } else {
                    this.boardService
                        .find(this.ownerTools.boardId)
                        .subscribe((subRes: Board) => {
                            this.boards = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.ownerTools.id !== undefined) {
            this.subscribeToSaveResponse(
                this.ownerToolsService.update(this.ownerTools), false);
        } else {
            this.subscribeToSaveResponse(
                this.ownerToolsService.create(this.ownerTools), true);
        }
    }

    private subscribeToSaveResponse(result: Observable<OwnerTools>, isCreated: boolean) {
        result.subscribe((res: OwnerTools) =>
            this.onSaveSuccess(res, isCreated), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: OwnerTools, isCreated: boolean) {
        this.alertService.success(
            isCreated ? 'wBoardApp.ownerTools.created'
            : 'wBoardApp.ownerTools.updated',
            { param : result.id }, null);

        this.eventManager.broadcast({ name: 'ownerToolsListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackUserById(index: number, item: User) {
        return item.id;
    }

    trackBoardById(index: number, item: Board) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-owner-tools-popup',
    template: ''
})
export class OwnerToolsPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private ownerToolsPopupService: OwnerToolsPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.ownerToolsPopupService
                    .open(OwnerToolsDialogComponent, params['id']);
            } else {
                this.modalRef = this.ownerToolsPopupService
                    .open(OwnerToolsDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
