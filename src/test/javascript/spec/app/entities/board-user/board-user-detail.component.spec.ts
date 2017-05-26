import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BoardUserDetailComponent } from '../../../../../../main/webapp/app/entities/board-user/board-user-detail.component';
import { BoardUserService } from '../../../../../../main/webapp/app/entities/board-user/board-user.service';
import { BoardUser } from '../../../../../../main/webapp/app/entities/board-user/board-user.model';

describe('Component Tests', () => {

    describe('BoardUser Management Detail Component', () => {
        let comp: BoardUserDetailComponent;
        let fixture: ComponentFixture<BoardUserDetailComponent>;
        let service: BoardUserService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [BoardUserDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BoardUserService,
                    EventManager
                ]
            }).overrideComponent(BoardUserDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BoardUserDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BoardUserService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new BoardUser(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.boardUser).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
