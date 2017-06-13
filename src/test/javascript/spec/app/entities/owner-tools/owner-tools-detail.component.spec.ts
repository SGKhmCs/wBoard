import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { WBoardTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { OwnerToolsDetailComponent } from '../../../../../../main/webapp/app/entities/owner-tools/owner-tools-detail.component';
import { OwnerToolsService } from '../../../../../../main/webapp/app/entities/owner-tools/owner-tools.service';
import { OwnerTools } from '../../../../../../main/webapp/app/entities/owner-tools/owner-tools.model';

describe('Component Tests', () => {

    describe('OwnerTools Management Detail Component', () => {
        let comp: OwnerToolsDetailComponent;
        let fixture: ComponentFixture<OwnerToolsDetailComponent>;
        let service: OwnerToolsService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [WBoardTestModule],
                declarations: [OwnerToolsDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    OwnerToolsService,
                    EventManager
                ]
            }).overrideTemplate(OwnerToolsDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(OwnerToolsDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(OwnerToolsService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new OwnerTools(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.ownerTools).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
