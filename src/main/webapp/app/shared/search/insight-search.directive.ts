import {
    ComponentFactory,
    ComponentFactoryResolver,
    ComponentRef,
    Directive,
    ElementRef,
    EventEmitter,
    Injector,
    Input,
    OnChanges,
    Output,
    SimpleChanges,
    ViewContainerRef
} from '@angular/core';
import { InsightSearchSuggestionComponent } from './insight-search-suggestion.component';
import { GenericModel } from '../model/generic.model';

@Directive({
    selector: '[insInsightSearch]'
})
export class InsightSearchDirective implements OnChanges {
    suggestions: GenericModel[];
    @Input()
    textFields: string[];
    @Input()
    symbolField: string;

    @Output()
    selectionEmitter: EventEmitter<any> = new EventEmitter();

    suggestComp: ComponentRef<InsightSearchSuggestionComponent>;

    constructor(private _el: ElementRef, private _resolver: ComponentFactoryResolver, private _vcr: ViewContainerRef) {}

    ngOnChanges(change: SimpleChanges) {}

    display() {
        if (this.suggestComp) {
            return;
        }
        const factory: ComponentFactory<InsightSearchSuggestionComponent> = this._resolver.resolveComponentFactory(
            InsightSearchSuggestionComponent
        );
        const injector = Injector.create({
            providers: [
                {
                    provide: 'directiveSelectionEmitter',
                    useValue: this.selectionEmitter
                }
            ]
        });
        this.suggestComp = this._vcr.createComponent(factory, 0, injector);
        this.suggestComp.instance.suggestions = this.suggestions;
        this.suggestComp.instance.textFields = this.textFields;
        this.suggestComp.instance.symbolField = this.symbolField;

        const rect: ClientRect = this._el.nativeElement.getBoundingClientRect();
        this.suggestComp.instance.dimension.top = this._el.nativeElement.offsetTop;
        this.suggestComp.instance.dimension.left = rect.left;
        this.suggestComp.instance.dimension.width = rect.width;
    }

    hide() {
        if (this.suggestComp == null || typeof this.suggestComp === 'undefined') {
            return;
        }
        this.suggestComp.destroy();
        this.suggestComp = null;
    }
}
