import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, AlertService, JhiLanguageService } from 'ng-jhipster';

import { Permission } from './permission.model';
import { PermissionPopupService } from './permission-popup.service';
import { PermissionService } from './permission.service';
import { User, UserService } from '../../shared';
import { Board, BoardService } from '../board';
import { PermissionType, PermissionTypeService } from '../permission-type';

@Component({
    selector: 'jhi-permission-dialog',
    templateUrl: './permission-dialog.component.html'
})
export class PermissionDialogComponent implements OnInit {

    permission: Permission;
    authorities: any[];
    isSaving: boolean;

    users: User[];

    boards: Board[];

    permissiontypes: PermissionType[];
    constructor(
        public activeModal: NgbActiveModal,
        private jhiLanguageService: JhiLanguageService,
        private alertService: AlertService,
        private permissionService: PermissionService,
        private userService: UserService,
        private boardService: BoardService,
        private permissionTypeService: PermissionTypeService,
        private eventManager: EventManager
    ) {
        this.jhiLanguageService.setLocations(['permission']);
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.userService.query().subscribe(
            (res: Response) => { this.users = res.json(); }, (res: Response) => this.onError(res.json()));
        this.boardService.query().subscribe(
            (res: Response) => { this.boards = res.json(); }, (res: Response) => this.onError(res.json()));
        this.permissionTypeService.query().subscribe(
            (res: Response) => { this.permissiontypes = res.json(); }, (res: Response) => this.onError(res.json()));
    }
    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.permission.id !== undefined) {
            this.permissionService.update(this.permission)
                .subscribe((res: Permission) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        } else {
            this.permissionService.create(this.permission)
                .subscribe((res: Permission) =>
                    this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
        }
    }

    private onSaveSuccess(result: Permission) {
        this.eventManager.broadcast({ name: 'permissionListModification', content: 'OK'});
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

    trackPermissionTypeById(index: number, item: PermissionType) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-permission-popup',
    template: ''
})
export class PermissionPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private permissionPopupService: PermissionPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.permissionPopupService
                    .open(PermissionDialogComponent, params['id']);
            } else {
                this.modalRef = this.permissionPopupService
                    .open(PermissionDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
