import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { ReaderDetailComponent } from '../../../../../../main/webapp/app/entities/reader/reader-detail.component';
import { ReaderService } from '../../../../../../main/webapp/app/entities/reader/reader.service';
import { Reader } from '../../../../../../main/webapp/app/entities/reader/reader.model';

describe('Component Tests', () => {

    describe('Reader Management Detail Component', () => {
        let comp: ReaderDetailComponent;
        let fixture: ComponentFixture<ReaderDetailComponent>;
        let service: ReaderService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [ReaderDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    ReaderService,
                    EventManager
                ]
            }).overrideComponent(ReaderDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ReaderDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ReaderService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Reader(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.reader).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
